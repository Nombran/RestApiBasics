package com.epam.esm.controller;

import com.epam.esm.certificate.dto.CertificateDto;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.tag.TagService;
import com.epam.esm.tag.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    private final TagService tagService;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 TagService tagService) {
        this.certificateService = certificateService;
        this.tagService = tagService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CertificateDto> findCertificates(@RequestParam(name = "tagName", required = false)
                                        String tagName,
                                        @RequestParam(name = "descriptionPart", required = false)
                                        String descriptionPart,
                                        @RequestParam(name = "orderBy", required = false)
                                        String orderBy
                                        ) {
        return certificateService.findCertificates(tagName, descriptionPart, orderBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CertificateDto certificate) {
        certificateService.create(certificate);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody CertificateDto certificate, @PathVariable(name = "id") long id) {
        certificate.setId(id);
        certificateService.update(certificate);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") long id) {
        if (!certificateService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Certificate with id = " + id + " doesn't exist");
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto findById(@PathVariable(name = "id") long id) {
        return certificateService.find(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "There is no certificate with id " + id)
        );
    }

    @GetMapping(value = "/{id}/tags")
    public List<Tag> findAllCertificateTags(@PathVariable(name = "id") long id) {
        return tagService.findTagsByCertificateId(id);
    }

    @PostMapping(value = "/{id}/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTag(@PathVariable(name = "id")long certificateId, @RequestBody Tag tag) {
            certificateService.addCertificateTag(tag, certificateId);
    }

    @DeleteMapping(value = "/{id}/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCertificateTag(@PathVariable(name = "id") long certificateId,
                                     @PathVariable(name = "tagId") long tagId) {
        certificateService.deleteCertificateTag(certificateId, tagId);
    }
}
