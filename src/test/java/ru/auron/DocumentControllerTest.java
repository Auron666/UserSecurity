package ru.auron;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.auron.model.Document;
import ru.auron.repository.DocumentRepository;
import ru.auron.service.PermissionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DocumentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentRepository documentRepository;

    @MockBean
    private PermissionService permissionService;


//    @Test
//    @WithMockUser(username = "admin")
//    public void adminShouldGetDocuments123() throws Exception {
//
//        Document d1=new Document(1, "Document 1");
//        Document d2=new Document(2, "Document 2");
//        Document d3=new Document(3, "Document 3");
//
//        mockMvc.perform(
//                        get("/document"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(d1,d2,d3))));
//        ;
//    }

//    @Test
//    @WithMockUser(username = "user1")
//    public void user1ShouldGetDocument1WithPathVariable() throws Exception {
//
//        Document d1=new Document(1, "Document 1");
//
//        when(documentRepository.save(any(Document.class))).thenReturn(d1);
//
//        mockMvc.perform(
//                        get("/document/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(d1)));
//
//    }

    @Test
    @WithMockUser(username = "user1")
    public void user1ShouldNotGetDocument2() throws Exception {

        mockMvc.perform(
                        get("/document/2"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "admin")
    public void adminShouldEditDocument() throws Exception {

        Document d1=new Document( 1,"Document 1 Edited");

        when(documentRepository.save(any(Document.class))).thenReturn(d1);

        mockMvc.perform(
                        put("/document/1")
                                .content(objectMapper.writeValueAsString(d1))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(d1)));
    }

    @Test
    @WithMockUser(username = "user1")
    public void user1ShouldNotEditDocument() throws Exception {

        Document d1=new Document( 1,"Document 1 Edited");

        mockMvc.perform(
                        put("/document/1")
                                .content(objectMapper.writeValueAsString(d1))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void adminShouldSetPermissionAndPostDocument() throws Exception {
        Document d4=new Document(2, "document 100");

        when(documentRepository.save(any(Document.class))).thenReturn(d4);

        mockMvc.perform(
                        post("/document")
                                .content(objectMapper.writeValueAsString(d4))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(d4)));

        verify(permissionService, times(2)).addPermissionForUser(any(), any(), any());

    }


}
