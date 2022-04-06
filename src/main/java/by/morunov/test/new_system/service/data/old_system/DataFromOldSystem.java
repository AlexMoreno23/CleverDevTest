package by.morunov.test.new_system.service.data.old_system;

import by.morunov.test.old_system.client.dto.ClientDto;
import by.morunov.test.old_system.note.dto.ClientNoteDto;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alex Morunov
 */
@Component
public class DataFromOldSystem {

    HttpClient httpClient = HttpClientBuilder.create().build();
    ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    private final RestTemplate restTemplate = new RestTemplate(requestFactory);
    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

    public List<ClientDto> getClients() {
        String urlClient = "http://localhost:8080/clients";
        try {
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);
            ClientDto[] response = restTemplate.getForObject(new URI(urlClient), ClientDto[].class);
            assert response != null;
            return Arrays.stream(response).collect(Collectors.toList());

        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }

    }

    public List<ClientNoteDto> getNotes() {
        String urlNote = "http://localhost:8080/notes/all";
        try {
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);
            ClientNoteDto[] response = restTemplate.getForObject(new URI(urlNote), ClientNoteDto[].class);
            assert response != null;
            return Arrays.stream(response).collect(Collectors.toList());

        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }

    }


}
