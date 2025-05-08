package com.demo.poc.commons.config;

import com.demo.poc.commons.service.OperationSelectorService;
import com.demo.poc.commons.service.OperationService;
import com.demo.poc.commons.spider.JenkinsManagementSpider;
import com.demo.poc.entrypoint.k8scloud.service.ConfigK8sCloudService;
import com.demo.poc.entrypoint.k8scloud.spider.ConfigK8sCloudSpider;
import com.demo.poc.entrypoint.k8scredential.service.AddK8sCredentialService;
import com.demo.poc.entrypoint.k8scredential.spider.AddK8sCredentialSpider;
import com.demo.poc.entrypoint.k8splugin.service.InstallK8SPluginService;
import com.demo.poc.entrypoint.k8splugin.spider.InstallK8sPluginSpider;
import com.demo.poc.entrypoint.login.service.LoginService;
import com.demo.poc.entrypoint.login.spider.LoginSpider;
import com.demo.poc.entrypoint.pipeline.service.PipelineCreationService;
import com.demo.poc.entrypoint.pipeline.spider.PipelineCreationSpider;
import com.demo.poc.entrypoint.unlock.service.InitialUnlockService;
import com.demo.poc.entrypoint.unlock.spider.SuggestedPluginSpider;
import com.demo.poc.entrypoint.unlock.spider.UnlockSpider;
import com.google.inject.AbstractModule;
import com.demo.poc.commons.properties.PropertiesReader;
import com.demo.poc.commons.helper.DriverHelper;
import com.google.inject.multibindings.Multibinder;

public class ComponentsConfig extends AbstractModule {

  @Override
  protected void configure() {
    //config
    bind(PropertiesReader.class).toInstance(new PropertiesReader());
    bind(DriverHelper.class);
    Multibinder<OperationService> binderSet = Multibinder.newSetBinder(binder(), OperationService.class);

    //commons
    bind(OperationSelectorService.class);
    bind(JenkinsManagementSpider.class);

    //initial unlock
    binderSet.addBinding().to(InitialUnlockService.class);
    bind(UnlockSpider.class);
    bind(SuggestedPluginSpider.class);

    //install k8s plugins
    binderSet.addBinding().to(InstallK8SPluginService.class);
    bind(InstallK8sPluginSpider.class);

    //login
    binderSet.addBinding().to(LoginService.class);
    bind(LoginSpider.class);

    //add k8s credentials
    binderSet.addBinding().to(AddK8sCredentialService.class);
    bind(AddK8sCredentialSpider.class);

    //config k8s cloud
    binderSet.addBinding().to(ConfigK8sCloudService.class);
    bind(ConfigK8sCloudSpider.class);

    //create pipeline
    binderSet.addBinding().to(PipelineCreationService.class);
    bind(PipelineCreationSpider.class);
  }
}
