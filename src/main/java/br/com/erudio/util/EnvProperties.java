package br.com.erudio.util;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;

@Sources({"classpath:env.properties"})
public interface EnvProperties extends Config {
	
    EnvProperties INSTANCE = ConfigFactory.create(EnvProperties.class);

    @Key("env")
    String env();

    @Key("host")
    String host();

    @Key("emailsPadroes")
    String emailsPadroes();

    @Key("emailsSempreRecebem")
    String emailsSempreRecebem();

    @Key("aws.accessKey")
    String awsAccessKey();

    @Key("aws.secretKey")
    String awsSecretKey();

    @Key("aws.endpoint")
    String awsEndpoint();

    @Key("aws.bucket")
    String awsBucket();
}
