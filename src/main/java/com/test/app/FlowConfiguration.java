package com.test.app;

import com.test.app.sleuth.SleuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

import static com.test.app.sleuth.SleuthConfig.TEST_BAGGAGE_FIELD_NAME;

@Configuration
public class FlowConfiguration {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private Tracer tracer;
	@Autowired
	private SleuthService sleuthService;


	@Bean
	public IntegrationFlow inFlow(){
		
		return IntegrationFlows.from(Http.inboundGateway("/customer/**")
				.requestMapping(r-> r.methods(HttpMethod.GET,HttpMethod.POST,HttpMethod.PUT,HttpMethod.OPTIONS,HttpMethod.DELETE))
				.replyChannel(getChannelBean("replyChannel"))
				.errorChannel(getChannelBean("errorChannel")))
				.channel(getChannelBean("requestChannel"))

				.handle((p, h) -> {
					tracer.createBaggage("mybaggage", "my-baggage-value");
					sleuthService.updateBaggageFieldValue(TEST_BAGGAGE_FIELD_NAME, "value");
					System.out.println(String.format("(TRACE_ID, SPAN_IN) -> (%s, %s)",
						tracer.currentSpan().context().traceId(), tracer.currentSpan().context().spanId()));
					System.out.println("BAGGAGE FIELDS: " + this.tracer.getAllBaggage().size());
					System.out.println("BAGGAGE FIELD: " + sleuthService.getBaggageFieldValue(TEST_BAGGAGE_FIELD_NAME));
					return p;
				})

				.handle((p, h) -> {
					System.out.println(String.format("(TRACE_ID, SPAN_IN) -> (%s, %s)",
						tracer.currentSpan().context().traceId(), tracer.currentSpan().context().spanId()));
					System.out.println("BAGGAGE FIELDS: " + this.tracer.getAllBaggage().size());
					System.out.println("BAGGAGE FIELD: " + sleuthService.getBaggageFieldValue(TEST_BAGGAGE_FIELD_NAME));
					return p;
				})
				.nullChannel();
	}
	
	@Bean
	public IntegrationFlow errorFlow(){
		return IntegrationFlows.from(getChannelBean("errorChannel"))
				.handle("errorHandler","handleError")
				.channel(getChannelBean("replyChannel"))				
				.get();
	}
	
	private MessageChannel getChannelBean(String channelName){
		return (MessageChannel) context.getBean(channelName);
	}

}
