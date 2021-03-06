package app.yapam.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileDao, String> {

    FileDao findOneByHash(String hash);

    FileDao findOneById(String fileId);
}
