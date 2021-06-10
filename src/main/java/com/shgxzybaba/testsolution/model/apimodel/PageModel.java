package com.shgxzybaba.testsolution.model.apimodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class PageModel {
    int page;
    @Min(value = 1, message = "Page size cannot be less than one")
    int pageSize;


}
