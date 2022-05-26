package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: 13936
 * @date: 2022/5/17 15:26
 * @description:
 */

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //1.拿数据
            Map result=reportService.getBusinessReportData();
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //2.获取模板文件
            //获得Excel模板文件绝对路径    (拼路径时‘/’使用：windows系统用\(\特殊需要转义字符\\表示)  linux系统用 / ，为了跨平台可以使用File.separator代替)
            String filepath=request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";

            //3.变成工作簿
            Workbook workbook=new XSSFWorkbook(new File(filepath));

            //4.写数据
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //5.输出文件，以流形式文件下载，另存为操作
            ServletOutputStream out = response.getOutputStream();

            //告诉浏览器返回的是什么样的数据（设置响应的数据类型）
            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)   名字report.xlsx（默认名）可以自定义
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            workbook.write(out);//写给浏览器，文件下载

            //6.关闭对象
            out.flush();
            out.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
            //跳转错误页面
            request.getRequestDispatcher("/pages/error/downloadError.html").forward(request,response);
        }
    }


    // 运营数据统计（页面）
    @RequestMapping(value = "/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


        // 统计套餐预约人数占比（饼图）
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            // 组织套餐名称+套餐名称对应的数据
            List<Map<String,Object>> setmealCount =  setmealService.getSetmealReport();

            Map<String, Object> map = new HashMap<>();
            map.put("setmealCount",setmealCount);
            // 组织套餐名称集合（格式：List<"尚硅谷三八节福利套餐","尚硅谷旅游套餐">）   不需要从后台调数据
            List<String> setmealNames = new ArrayList<>();

            for (Map<String, Object> m : setmealCount) {
                String name = (String) m.get("name");
                setmealNames.add(name);
            }
            map.put("setmealNames",setmealNames);
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }


    /**
     * 会员数量统计
     *  月份数据通过程序代码生成，会员数量时通过查询数据库获取
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            // 获取日历实例对象
            Calendar calendar = Calendar.getInstance();
            //根据当前时间，获取前12个月的日历(当前日历2020-02，12个月前，日历时间2019-03)
            //第一个参数，日历字段
            //第二个参数，要添加到字段中的日期或时间
            //-12为当前时间向前偏移
            calendar.add(Calendar.MONTH,-12);

            List<String> months = new ArrayList<String>();
            for(int i=0;i<12;i++){
                //第一个参数是月份 2018-7
                //第二个参数是月份+1个月
                calendar.add(Calendar.MONTH,1);
                months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            }

            Map<String,Object> map = new HashMap<String,Object>();
            // 把过去12个月的日期存储到map里面
            map.put("months",months);
            // 查询所有的会员
            List<Integer> memberCount = memberService.findMemberCountByMonth(months);
            map.put("memberCount",memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
}


