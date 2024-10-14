package guru.springframework.apifirst.apifirstserver.config;

//@Configuration
public class OpenApiValidationConfig {

    public static final String OA3_SPEC = "https://api.redocly.com/registry/bundle/learncome/API%20First%20With%20Spring%20Boot%20-%20Development/v1/openapi.yaml?branch=development";

//    @Bean
//    public Filter validationFilter() {
//        return new OpenApiValidationFilter(
//                true,
//                true
//        );
//    }
//
//    @Bean
//    public WebMvcConfigurer openApiValidationInterceptor() {
//
//        OpenApiInteractionValidator validator = OpenApiInteractionValidator.createForSpecificationUrl(OA3_SPEC)
//                .build();
//
//        OpenApiValidationInterceptor interceptor = new OpenApiValidationInterceptor(validator);
//        return new WebMvcConfigurer() {
//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//                registry.addInterceptor(interceptor);
//            }
//        };
//    }
}
