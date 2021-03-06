package com.soft1841.book.controller;

import cn.hutool.db.Entity;
import com.soft1841.book.dao.BookDAO;
import com.soft1841.book.dao.TypeDAO;
import com.soft1841.book.entity.Type;
import com.soft1841.book.service.BookService;
import com.soft1841.book.service.TypeService;
import com.soft1841.book.utils.DAOFactory;
import com.soft1841.book.utils.ServiceFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BookAnalysisController implements Initializable {
    @FXML
    private StackPane pieChartPane, barChartPane;

    private TypeService typeService = ServiceFactory.getTypeServiceInstance();
    private BookService bookService = ServiceFactory.getBookServiceInstance();
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPieChart();
        initBarChart();
    }

    private void initPieChart() {
        List<Type> typeList = typeService.getAllTypes();
        for (Type type : typeList) {
            int count = bookService.countByType(type.getId());
            pieChartData.add(new PieChart.Data(type.getTypeName(), count));
        }
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("按图书类别统计饼图");
        pieChartPane.getChildren().add(chart);
    }

    private void initBarChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc =
                new BarChart<>(xAxis, yAxis);
        bc.setTitle("根据类别统计柱形图");
        xAxis.setLabel("图书类别");
        yAxis.setLabel("图书数量");
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2018年统计数据");
        List<Type> typeList = typeService.getAllTypes();
        for (Type type : typeList) {
            int count = bookService.countByType(type.getId());
            series1.getData().add(new XYChart.Data(type.getTypeName(), count));
        }
        bc.getData().addAll(series1);
        barChartPane.getChildren().add(bc);
    }
}
