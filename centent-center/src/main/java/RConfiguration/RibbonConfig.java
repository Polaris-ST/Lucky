package RConfiguration;

import com.itmuch.cententcenter.Config.NacosSameClusterWeightRule;
import com.itmuch.cententcenter.Config.NacosWeightedRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfig {
    @Bean
    public IRule getRule() {
        return new NacosSameClusterWeightRule();
    }
}
