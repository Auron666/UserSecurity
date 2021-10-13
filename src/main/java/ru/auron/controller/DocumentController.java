package ru.auron.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.auron.model.Document;
import ru.auron.repository.DocumentRepository;
import ru.auron.service.PermissionService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final PermissionService permissionService;

    @GetMapping("/document")
    public List<Document> getAll(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info(principal.toString());
        return documentRepository.findAll();
    }

    @GetMapping("/document/{id}")
    public Document getById(@PathVariable("id") Integer id){
        return documentRepository.getById(id);
    }

    @PreAuthorize("hasPermission(#document, 'WRITE')")
    @PutMapping("/document/{id}")
    public Document edit(@PathVariable("id") Integer id, @RequestBody Document document){
        document.setId(id);
        return documentRepository.save(document);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/document")
    public Document post(@RequestBody Document document, Authentication authentication){

        Document result = documentRepository.save(document);

        permissionService.addPermissionForUser(result, BasePermission.WRITE, authentication.getName());
        permissionService.addPermissionForUser(result, BasePermission.READ, authentication.getName());

        return result;
    }




}
