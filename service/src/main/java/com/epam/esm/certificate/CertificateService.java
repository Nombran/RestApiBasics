package com.epam.esm.certificate;

import com.epam.esm.certificate.dao.CertificateDao;
import com.epam.esm.certificate.model.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    private final CertificateDao certificateDao;

    @Autowired
    public CertificateService(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    public boolean create(Certificate certificate) {
        return certificateDao.create(certificate);
    }

    public boolean update(Certificate certificate) {
        return certificateDao.update(certificate);
    }

    public boolean delete(long id) {
        return certificateDao.delete(id);
    }

    public Optional<Certificate> find(long id) {
        return certificateDao.find(id);
    }

    public List<Certificate> findAll() {
        return certificateDao.findAll();
    }
}
