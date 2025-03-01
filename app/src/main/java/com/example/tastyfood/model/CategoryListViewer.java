package com.example.tastyfood.model;

import java.util.List;

public interface CategoryListViewer {
    void onSuccess(List<Category> CategoryList);
    void onFailed();
}