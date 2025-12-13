package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.Car;
import web.service.CarService;

import java.util.Arrays;
import java.util.List;

@Controller
public class CarController {
    @Autowired
    CarService carService;

    @GetMapping(value = "/cars")
    public String printCars(@RequestParam(defaultValue = "5") int count, Model model) {
        List<Car> myCarList = Arrays.asList(
                new Car("Audi", "Black", 2),
                new Car("BMW", "White", 5),
                new Car("Mercedes", "Black", 12),
                new Car("Toyota", "Yellow", 2),
                new Car("Hyundai", "Red", 10)
        );
        List<Car> resultList = carService.getListByLimit(myCarList, count);
        model.addAttribute("cars", resultList);
        return "car-table";
    }
}
