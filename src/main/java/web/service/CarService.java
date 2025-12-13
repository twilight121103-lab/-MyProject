package web.service;

import org.springframework.stereotype.Service;
import web.model.Car;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    public List<Car> getListByLimit(List<Car> list, int limit) {
        if (limit > 5) {
            limit = 5;
        }
        return list.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
