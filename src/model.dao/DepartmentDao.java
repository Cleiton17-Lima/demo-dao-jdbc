package model.dao;

import  java.util.list;
import model.entities.Department;

public interface DepartmentDao {

    void insert(Seller obj);
    void update(Seller obj);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll;

    
} 