//package com.xuecheng.content;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.xuecheng.base.model.PageParams;
//import com.xuecheng.base.model.PageResult;
//import com.xuecheng.content.mapper.CourseBaseMapper;
//import com.xuecheng.content.model.dto.QueryCourseParamsDto;
//import com.xuecheng.content.model.po.CourseBase;
//import com.xuecheng.content.service.CourseBaseInfoService;
//import org.apache.commons.lang3.StringUtils;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
///**
// * @author Mr.M
// * @version 1.0
// * @description TODO
// * @date 2023/2/12 9:24
// */
//@SpringBootTest
//public class CourseBaseInfoServiceTests {
//
//    @Autowired
//    CourseBaseInfoService courseBaseInfoService;
//
//    @Test
//    public void testCourseBaseInfoService() {
//
//        //查询条件
//        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
//        courseParamsDto.setCourseName("java");//课程名称查询条件
//        courseParamsDto.setAuditStatus("202004");//202004表示课程审核通过
//        //分页参数对象
//        PageParams pageParams = new PageParams();
//        pageParams.setPageNo(2L);
//        pageParams.setPageSize(2L);
//
//        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(null,pageParams, courseParamsDto);
//        System.out.println(courseBasePageResult);
//
//    }
//}
package com.xuecheng.content;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.impl.CourseBaseInfoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 实验2 & 3：白盒测试
 * 测试对象：CourseBaseInfoServiceImpl.queryCourseBaseList
 * 技术栈：JUnit5 + Mockito
 */
@ExtendWith(MockitoExtension.class) //启用 Mockito 扩展
public class CourseBaseInfoServiceTests {

    // 1. 模拟 Mapper (因为 Service 中 @Autowired 了这三个 Mapper)
    @Mock
    CourseBaseMapper courseBaseMapper;

    @Mock
    CourseMarketMapper courseMarketMapper;

    @Mock
    CourseCategoryMapper courseCategoryMapper;

    // 2. 注入模拟对象到 Service 实现类中
    @InjectMocks
    CourseBaseInfoServiceImpl courseBaseInfoService;

    /**
     * 测试用例 1：基本查询测试
     * 场景：查询条件对象为空 (null)，验证是否能处理并调用 Mapper
     * 覆盖路径：if (courseParamsDto == null) -> true 的分支
     */
    @Test
    public void testQueryCourseBaseList_NullParams() {
        // 准备输入参数
        PageParams pageParams = new PageParams(1L, 10L);
        Long companyId = 1232141425L;

        // 模拟数据库返回的数据
        Page<CourseBase> mockPageResult = new Page<>();
        mockPageResult.setRecords(Collections.singletonList(new CourseBase())); // 设置一条假记录
        mockPageResult.setTotal(100);

        // 设定 Mock 行为：当调用 selectPage 时，无论传什么参数，都返回 mockPageResult
        Mockito.when(courseBaseMapper.selectPage(Mockito.any(), Mockito.any()))
                .thenReturn(mockPageResult);

        // 执行测试方法 (传入 null 的 DTO)
        PageResult<CourseBase> result = courseBaseInfoService.queryCourseBaseList(companyId, pageParams, null);

        // 断言验证
        Assertions.assertNotNull(result);
        Assertions.assertEquals(100, result.getCounts());
        Assertions.assertEquals(1, result.getItems().size());

        // 验证 selectPage 是否确实被调用了一次
        Mockito.verify(courseBaseMapper, Mockito.times(1)).selectPage(Mockito.any(), Mockito.any());
    }

    /**
     * 测试用例 2：条件查询测试
     * 场景：传入具体的课程名称和审核状态
     * 覆盖路径：queryWrapper 拼接条件的各个 if 分支
     */
    @Test
    public void testQueryCourseBaseList_WithConditions() {
        // 准备输入参数
        PageParams pageParams = new PageParams(1L, 10L);
        Long companyId = 1232141425L;

        // 构造详细的查询条件
        QueryCourseParamsDto dto = new QueryCourseParamsDto();
        dto.setCourseName("Spring Cloud"); // 触发 StringUtils.isNotEmpty(courseName) 分支
        dto.setAuditStatus("202004");      // 触发 StringUtils.isNotEmpty(auditStatus) 分支

        // 模拟数据库返回
        Page<CourseBase> mockPageResult = new Page<>();
        mockPageResult.setRecords(Arrays.asList(new CourseBase(), new CourseBase())); // 返回两条数据
        mockPageResult.setTotal(2);

        Mockito.when(courseBaseMapper.selectPage(Mockito.any(), Mockito.any()))
                .thenReturn(mockPageResult);

        // 执行测试
        PageResult<CourseBase> result = courseBaseInfoService.queryCourseBaseList(companyId, pageParams, dto);

        // 断言
        Assertions.assertEquals(2, result.getItems().size());

        // 验证 Mapper 被调用
        Mockito.verify(courseBaseMapper).selectPage(Mockito.any(), Mockito.any());
    }
}