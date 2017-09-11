package br.com.encurtadorurl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.encurtadorurl.model.Url;

/** Repositório DAO da Classe Url 
 * @author Murillo Santana
 * @version 1.0.0
 */

public interface EncurtadorUrlDAO extends JpaRepository<Url, Long>{

	@Query(value = "SELECT * "
				 + "FROM url "
				 + "WHERE alias = :aliasFind",
		   nativeQuery = true)
	Url findAlias(@Param("aliasFind") String aliasFind);
	
	@Query(value = "SELECT * "
				 + "FROM url "
				 + "WHERE url_original = :urlFind ",
			nativeQuery = true)
	Url findUrlOriginal(@Param("urlFind") String urlFind);
	
	@Query(value = "SELECT * "
			     + "FROM url "
			     + "ORDER BY access DESC limit 10",
			nativeQuery = true)
	List<Url> findUrlsMostAccessed();

}
