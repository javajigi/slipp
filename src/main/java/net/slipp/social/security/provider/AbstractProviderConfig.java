package net.slipp.social.security.provider;

import javax.annotation.PostConstruct;

import net.slipp.social.security.ConnectInterceptorList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.web.ConnectInterceptor;

public abstract class AbstractProviderConfig<S> {


    @Autowired
    @Qualifier("connectInterceptorList")
    private ConnectInterceptorList connectInterceptorList;

    protected abstract ConnectInterceptor<S> getConnectInterceptor();

    @PostConstruct
    public void register() {
        connectInterceptorList.add(getConnectInterceptor());
    }
}
