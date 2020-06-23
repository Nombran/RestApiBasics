package com.epam.esm.controller;

import com.epam.esm.certificate.dto.CertificateDto;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.tag.service.TagService;
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

import javax.validation.Valid;
import java.util.List;

/**
 * Class CertificateController for Rest Api Basics Task.
 *
 * @author ARTSIOM BERASTSEN
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/api/v1/certificates")
public class CertificateController {

    /**
     * Field certificateService
     */
    private final CertificateService certificateService;

    /**
     * Field tagService
     */
    private final TagService tagService;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 TagService tagService) {
        this.certificateService = certificateService;
        this.tagService = tagService;
    }

    /**
     * @param tagName
     * @param descriptionPart
     * @param orderBy
     * @return
     */
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
    public void update(@Valid @RequestBody CertificateDto certificate, @PathVariable("id") long id) {
        certificate.setId(id);
        certificateService.update(certificate);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        certificateService.delete(id);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto findById(@PathVariable("id") long id) {
        return certificateService.find(id);
    }

    @GetMapping(value = "/{id}/tags")
    public List<Tag> findAllCertificateTags(@PathVariable("id") long id) {
        return tagService.findTagsByCertificateId(id);
    }

    @PostMapping(value = "/{id}/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTag(@PathVariable("id") long certificateId, @RequestBody Tag tag) {
        certificateService.addCertificateTag(tag, certificateId);
    }

    @DeleteMapping(value = "/{id}/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCertificateTag(@PathVariable("id") long certificateId,
                                     @PathVariable("tagId") long tagId) {
        certificateService.deleteCertificateTag(certificateId, tagId);
    }
}
