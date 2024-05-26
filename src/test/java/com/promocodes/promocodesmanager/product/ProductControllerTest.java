package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.ExceptionMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ExceptionMapper exceptionMapper;

    @Autowired
    private JacksonTester<ProductDto> jacksonTester;

    @Test
    public void shouldReturnProductsDtoList() throws Exception {
        List<Product> products = new ArrayList<>(
                Arrays.asList(
                        new Product(
                                123L,
                                "Apple",
                                "Fruit",
                                1.12,
                                "USD"
                        ),
                        new Product(
                                233L,
                                "Banana",
                                "Fruit",
                                0.75,
                                "USD"
                        )
                )
        );

        List<ProductResponseDto> productsDtoList = new ArrayList<>(
                Arrays.asList(
                        new ProductResponseDto(
                                123L,
                                "Apple",
                                "Fruit",
                                1.12,
                                "USD"
                        ),
                        new ProductResponseDto(
                                233L,
                                "Banana",
                                "Fruit",
                                0.75,
                                "USD"
                        )
                )
        );

        when(productService.getAllProducts())
                .thenReturn(products);
        when(productMapper.toProductsDtoList(products))
                .thenReturn(productsDtoList);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/products"))
                .andExpect(MockMvcResultMatchers.status()
                        .is(200))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].id", Matchers.is(123)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].name", Matchers.is("Apple")))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].description", Matchers.is("Fruit")))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].price", Matchers.is(1.12)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].currency", Matchers.is("USD")))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[1].id", Matchers.is(233)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[1].name", Matchers.is("Banana")))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[1].description", Matchers.is("Fruit")))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[1].price", Matchers.is(0.75)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[1].currency", Matchers.is("USD")));

        verify(productService, times(1))
                .getAllProducts();
        verify(productMapper, times(1))
                .toProductsDtoList(products);
    }

    @Test
    public void shouldReturnEmptyProductsDtoListIfProductsListIsNullable() throws Exception {
        List<Product> products = null;
        List<ProductResponseDto> productsDtoList = new ArrayList<>();

        when(productService.getAllProducts())
                .thenReturn(products);
        when(productMapper.toProductsDtoList(products))
                .thenReturn(productsDtoList);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/products"))
                .andExpect(MockMvcResultMatchers.status()
                        .is(200))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()", Matchers.is(0)));

        verify(productService, times(1))
                .getAllProducts();
        verify(productMapper, times(1))
                .toProductsDtoList(products);
    }

    @Test
    public void shouldReturnEmptyProductsDtoListIfProductsListIsEmpty() throws Exception {
        List<Product> products = new ArrayList<>();
        List<ProductResponseDto> productsDtoList = new ArrayList<>();

        when(productService.getAllProducts())
                .thenReturn(products);
        when(productMapper.toProductsDtoList(products))
                .thenReturn(productsDtoList);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/products"))
                .andExpect(MockMvcResultMatchers.status()
                        .is(200))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()", Matchers.is(0)));

        verify(productService, times(1))
                .getAllProducts();
        verify(productMapper, times(1))
                .toProductsDtoList(products);
    }

    @Test
    public void shouldAddNewProduct() throws Exception {
        ProductDto productDto = new ProductDto(
                "Apple",
                "Fruit",
                1.12,
                "USD"
        );
        String jsonDto = jacksonTester.write(productDto).getJson();

        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo("Product has been successfully added");

        verify(productService, times(1))
                .addNewProduct(
                        productDto.getName(),
                        productDto.getDescription(),
                        productDto.getPrice(),
                        productDto.getCurrency());
    }
}