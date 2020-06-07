package com.epam.esm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan({ "com.epam.esm.**" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringConfig {

}
