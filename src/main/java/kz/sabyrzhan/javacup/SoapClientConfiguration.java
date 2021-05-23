package kz.sabyrzhan.javacup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class SoapClientConfiguration {
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath("kz.sabyrzhan.wsdl");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller jaxb2Marshaller, WebServiceMessageSender webServiceMessageSender) {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(jaxb2Marshaller);
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
        webServiceTemplate.setDefaultUri(Constants.WS_URL);
        webServiceTemplate.setMessageSender(webServiceMessageSender());

        return webServiceTemplate;
    }

    @Bean
    public WebServiceMessageSender webServiceMessageSender() {
        final int TIMEOUT = 5000;
        HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
        // timeout for creating a connection
        httpComponentsMessageSender.setConnectionTimeout(TIMEOUT);
        // when you have a connection, timeout the read blocks for
        httpComponentsMessageSender.setReadTimeout(TIMEOUT);

        return httpComponentsMessageSender;
    }

    @Bean
    public SoapClient countryClient(WebServiceTemplate webServiceTemplate) {
        SoapClient client = new SoapClient();
        client.setDefaultUri(Constants.WS_URL);
        client.setWebServiceTemplate(webServiceTemplate);
        return client;
    }
}
