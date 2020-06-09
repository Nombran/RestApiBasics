package com.epam.esm.controller;

import com.epam.esm.certificate.CertificateService;
import com.epam.esm.model.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Certificate> findAll() {
        return certificateService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Certificate certificate) {
        if (!certificateService.create(certificate)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error in creating certificate");
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Certificate certificate, @PathVariable(name = "id") long id) {
        certificate.setId(id);
        if (!certificateService.update(certificate)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error in updating certificate");
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") long id) {
        if (!certificateService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete certificate with id = " + id);
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Certificate findById(@PathVariable(name = "id") long id) {
        return certificateService.find(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "There is no certificate with id " + id)
        );
    }
}
