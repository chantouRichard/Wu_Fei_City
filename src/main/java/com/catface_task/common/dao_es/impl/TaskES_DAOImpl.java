package com.catface_task.common.dao_es.impl;


import com.alibaba.fastjson.JSON;
import com.catface_task.common.dao_es.TaskES_DAO;
import com.catface_task.common.model_es.TaskES;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @className: TaskES_DAOimpl
 * @author: Havoc.
 * @date: 2024/12/2 00:25
 * @version: 0.1
 * @description:
 */
@Repository
public class TaskES_DAOImpl implements TaskES_DAO {

    @Autowired
    private ElasticsearchOperations esOperations;

    private static final Logger logger = LoggerFactory.getLogger(TaskES_DAOImpl.class);

    @Override
    public boolean addTask(TaskES task) {
        try {
            esOperations.save(task);  // TODO 这里如何检查 // QUES 检查什么？
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<TaskES> searchTasksByKeywords(Integer num, Integer skip, String query) {
        // TIP 注入问题？应该不存在，会被直接分词？但确实有一定的注入问题。
        Query esQuery = getQuery(query);

        try { // UPDATE 其实好像不需要这么复杂的转换。
            SearchHits<Map> searchHits = esOperations.search(esQuery, Map.class, IndexCoordinates.of("catface_tasks"));
            List<TaskES> taskESList = new ArrayList<>();

            for (SearchHit<Map> hit : searchHits) {
                Map<String, Object> content = hit.getContent();
                TaskES taskES = JSON.parseObject(JSON.toJSONString(content), TaskES.class);
                taskESList.add(taskES);
                logger.info("Hit: {}", taskES);
            }

            return taskESList;
        } catch (Exception e) {
            // 记录日志
            logger.error("Error while searching tasks by keywords: ", e);
            // 可以选择返回空结果或其他默认值
            return new ArrayList<>();
        }
    }

    @NotNull
    private static Query getQuery(String query) {
        String body = String.format("""
            {
               "bool": {
                    "should": [
                        {"match": {"title": "%s"}},
                        {"match": {"description": "%s"}},
                        {"match": {"tags": "%s"}}
                    ]
                }
            }
            """, query, query, query); //, 0, query, query, query, query, Constants.PreTags, Constants.PostTags);

        Query esQuery = new StringQuery(body);
        // 字段过滤
        FetchSourceFilter sourceFilter = new FetchSourceFilter(new String[]{"id", "title", "description", "position", "tags"}, null);
        esQuery.addSourceFilter(sourceFilter);
        // TODO 根据插入时间排序。
        return esQuery;
    }

    @Override
    public List<TaskES> vectorSearch(float[] queryVector, int size) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String queryVectorJson = mapper.writeValueAsString(queryVector);
            String query = String.format("""
                {
                    "script_score": {
                        "query": {"match_all": {}},
                        "script": {
                            "source": "cosineSimilarity(params.query_vector, 'embedding') + 1.0",
                            "params": {"query_vector": %s}
                        }
                    }
                }
                """, queryVectorJson);
            Pageable topK = PageRequest.of(0, 2);

            Query esQuery = new StringQuery(query, topK);

            SearchHits<Map> searchHits = esOperations.search(esQuery, Map.class, IndexCoordinates.of("catface_tasks"));
            List<TaskES> taskESList = new ArrayList<>();

            for (SearchHit<Map> hit : searchHits) {
                Map<String, Object> content = hit.getContent();
                TaskES taskES = JSON.parseObject(JSON.toJSONString(content), TaskES.class);
                taskESList.add(taskES);
                logger.info("Hit: {}", taskES);
            }

            return taskESList;
        } catch (IOException e) {
            logger.error("Error converting query vector to JSON: ", e);
            return null;
        } catch (Exception e) {
            // 记录日志
            logger.error("Error while searching tasks by keywords: ", e);
            // 可以选择返回空结果或其他默认值
            return new ArrayList<>();
        }
    }

//    @Override
//    public SearchHits<TaskES> searchTasksByKeywords(Integer num, Integer skip, String query) {
//        Query esQuery = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder -> queryBuilder
//                        .bool(b -> b
//                                .should(s -> s.match(m -> m.field("title").query(query)))
//                                .should(s -> s.match(m -> m.field("description").query(query)))
//                                .should(s -> s.match(m -> m.field("position").query(query)))
//                                .should(s -> s.match(m -> m.field("tags").query(query)))
//                        )
//                )
//                .withHighlightFields(
//                        new HighlightBuilder.Field("title").preTags(Constants.PreTags).postTags(Constants.PostTags),
//                        new HighlightBuilder.Field("description").preTags(Constants.PreTags).postTags(Constants.PostTags),
//                        new HighlightBuilder.Field("position").preTags(Constants.PreTags).postTags(Constants.PostTags),
//                        new HighlightBuilder.Field("tags").preTags(Constants.PreTags).postTags(Constants.PostTags)
//                )
//                .withSourceFilter(new FetchSourceFilter(new String[]{"id", "title", "description", "position", "tags"}, null))
//                .withPageable(PageRequest.of(skip / num, num))
//                .build();
//
//        try {
//            return esOperations.search(esQuery, TaskES.class);
//        } catch (Exception e) {
//            // 记录日志
//            logger.error("Error while searching tasks by keywords: ", e);
//            // 可以选择返回空结果或其他默认值
//            return null;
//        }
//    }

}
