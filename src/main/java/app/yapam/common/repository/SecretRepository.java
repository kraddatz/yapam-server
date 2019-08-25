package app.yapam.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SecretRepository extends JpaRepository<SecretDao, String> {

    @Transactional
    void deleteBySecretId(String secretId);

    SecretDao findFirstBySecretIdAndVersion(String secretId, Integer version);

    SecretDao findFirstBySecretIdOrderByVersionDesc(String secretId);

    SecretVersionProjection findFirstDistinctVersionBySecretIdOrderByVersionDesc(String secretId);

}
