/*
package javase.iodemo.jsontoexcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

public class ConvertUtil {

    public static String convertTextBodyToBean(Reader reader) {
        BinaryOperator<StringBuffer> accumulator = StringBuffer::append;
        return withLinesOf(reader,
                lines -> lines.map(StringBuffer::new)
                        .reduce(accumulator)
                        .orElseThrow(RuntimeException::new)
                        .toString(),
                RuntimeException::new);
    }

    private static <T> T withLinesOf(final Reader input,
                                     final Function<Stream<String>, T> handler,
                                     final Function<IOException, RuntimeException> error) {
        try (BufferedReader reader = new BufferedReader(input)) {
            return handler.apply(reader.lines());
        } catch (IOException e) {
            throw error.apply(e);
        }
    }

    public static Document convertListMap2Document(Map<String, Object> map) {
        Document doc = new Document();
        if (null == map || map.size() == 0) {
            return doc;
        }
        map.forEach(doc::append);
        return doc;
    }

    public static List<Document> convertListMap2Documents(List<Map<String, Object>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<Document> docs = Lists.newArrayListWithCapacity(list.size());
        if (list.size() == 0) {
            return docs;
        }
        for (Map<String, Object> map : list) {
            Document doc = new Document();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                doc.append(entry.getKey(), entry.getValue());
            }
            docs.add(doc);
        }
        return docs;
    }

    public static List<Map<String, Object>> convertDocuments2ListMap(List<Document> docs) {
        if (CollectionUtils.isEmpty(docs)) {
            return new ArrayList<>(0);
        }
        List<Map<String, Object>> list = Lists.newArrayListWithCapacity(docs.size());
        if (docs.size() == 0) {
            return list;
        }
        for (Document doc : docs) {
            Map<String, Object> map = new HashMap<>(JSON.parseObject(doc.toJson()));
            list.add(map);
        }
        return list;
    }

    public static Map<String, Object> convertDocument2Map(Document doc) {
        Map<String, Object> map = Maps.newHashMap();
        if (null == doc) {
            return map;
        }
        return new HashMap<>(JSON.parseObject(doc.toJson()));
    }

    public static Document cloneDocument(Document doc) {
        if (null == doc) {
            return new Document();
        }
        return new Document(JSON.parseObject(doc.toJson()));
    }


    public static Document parseJSONObjctToDocument(JSONObject json) {
        if (null == json) {
            return new Document();
        }
        return new Document(json);
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return result;
    }

    public static <T> T caseEntity(Object obj, Class<T> clazz) {
        if (obj != null) {
            return clazz.cast(obj);
        }
        return null;
    }

}
*/
