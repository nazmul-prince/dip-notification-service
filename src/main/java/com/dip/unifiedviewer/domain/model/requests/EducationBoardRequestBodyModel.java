package com.dip.unifiedviewer.domain.model.requests;

import com.dip.unifiedviewer.constansts.CommonConstants;
import com.dip.unifiedviewer.constansts.JsonConstants;
import com.dip.unifiedviewer.constansts.SelectionCriteria;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(callSuper = true)
public class EducationBoardRequestBodyModel extends BaseRequestBodyModel{
    @NotBlank(message = "board name can not be null")
    private String boardName;
    @NotNull(message = "passing year can not be null")
    private Integer passingYear;
    @NotBlank(message = "exam name can not be null")
    private String examName;
    @NotNull(message = "roll No. can not be null")
    private Integer rollNo;
    @NotNull(message = "registration No. can not be null")
    private String registrationNo;

    private Integer page = CommonConstants.PAGE_NO;

    private Integer size = CommonConstants.PAGE_SIZE;

    private Integer selectionCriteria = SelectionCriteria.EDUCATION_BOARD;

    private String requestData = SelectionCriteria.EDUCATION_BOARD_REQUEST_DATA_TYPE;
}
