package io.gwola.boot.dao.elasticsearch;

import io.gwola.boot.entity.elasticsearch.EsLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


/**
 * @author Exrickx
 */
public interface EsLogDao extends ElasticsearchRepository<EsLog, String> {

}