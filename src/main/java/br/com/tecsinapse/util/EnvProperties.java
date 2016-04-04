package br.com.tecsinapse.util;

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

    /**
     * @return Emails padrões separados por virgula
     */
    @Key("emailsPadroes")
    String emailsPadroes();

    /**
     * @return Emails que sempre recebem separados por virgula
     */
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
