package com.csf.persistance.dao;

import java.util.List;

import javax.transaction.Transactional;

@Transactional
public interface GenericDao<T , I> {

  List<T> findAll();


  T find(I id);


  T save(T entity);


  void delete(I id);

}
