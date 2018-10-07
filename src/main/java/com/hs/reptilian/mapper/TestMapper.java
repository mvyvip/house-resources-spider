package com.hs.reptilian.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TestMapper {

    List<Map<String, Object>> findAllOrder();

    void update(@Param("userId") String userId, @Param("amount") BigDecimal amount);

    void saveHs(@Param("city") String city, @Param("area") String area, @Param("address") String address, @Param("village") String village);
}
