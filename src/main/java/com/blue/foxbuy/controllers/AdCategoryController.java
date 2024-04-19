package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.AdCategory;
import com.blue.foxbuy.models.DTOs.AdCategoryDTO;
import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.services.AdCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
@Tag(name = "Ad categories endpoints")
public class AdCategoryController {

    private final AdCategoryService adCategoryService;

    @Autowired
    public AdCategoryController(AdCategoryService adCategoryService) {
        this.adCategoryService = adCategoryService;
    }

    @Operation(
            description = "Accepts the name of the category to be created " +
                    "and it's description, then checks if the name is available.",
            summary = "Endpoint for creating ad categories. - Admin only",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "400",
                            description = "Returned when the name is not present",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : '[List of errors displayed here]'}")
                            )),
                    @ApiResponse(responseCode = "409",
                            description = "Returned when the name is already used",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Category already exists'}")
                            )),
                    @ApiResponse(responseCode = "201",
                            description = "Returned when the category is successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AdCategory.class),
                                    examples = @ExampleObject(value = "{'id' : '1'," +
                                            " 'name' : 'IT', " +
                                            " 'description' : 'All technical gizmos and gadgets'}")
                            ))
            }
    )
    @PostMapping
    @SecurityRequirement(name = "BearerToken")
    public ResponseEntity<?> createCategory(@Valid @RequestBody AdCategoryDTO adCategoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(errors.toString()));
        }
        AdCategory createdCategory;
        if (adCategoryService.doesCategoryExist(adCategoryDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Category already exists"));
        } else {
            createdCategory = adCategoryService.createCategory(adCategoryDTO);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @Operation(
            description = "Checks if the category of the given ID exists, " +
                    "if it does it is updated and returned as a response.",
            summary = "Endpoint for updating ad categories. - Admin only",
            method = "PUT",
            responses = {
                    @ApiResponse(responseCode = "400",
                            description = "Returned when the name is not present",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : '[List of errors displayed here]'}")
                            )),
                    @ApiResponse(responseCode = "404",
                            description = "Returned when the category is not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Category does not exist'}")
                            )),
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the category is successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AdCategory.class),
                                    examples = @ExampleObject(value = "{'id' : '1'," +
                                            " 'name' : 'IT', " +
                                            " 'description' : 'All technical gizmos and gadgets'}")
                            ))
            }
    )
    @PutMapping("/{id}")
    @SecurityRequirement(name = "BearerToken")
    public ResponseEntity<?> updateCategory(
                                            @Valid @RequestBody AdCategoryDTO adCategoryDTO,
                                            BindingResult bindingResult,
                                            @Parameter(name = "id", description = "Id of the category to be updated", example = "1") @PathVariable(value = "id") Integer id
                                            ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(errors.toString()));
        }
        AdCategory updatedCategory;
        if (!adCategoryService.doesCategoryExist(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Category does not exist"));
        } else {
            updatedCategory = adCategoryService.updateCategory(id, adCategoryDTO);
        }
        return ResponseEntity.ok().body(updatedCategory);
    }

    @Operation(
            description = "Checks to see if the category of the given ID exists, " +
            "if it does checks to see if there are any ads associated with it, " +
            "if there are move all the ads to 'Uncategorized'.",
            summary = "Endpoint for deleting ad categories. - Admin only",
            method = "DELETE",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Returned when the category is not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Category does not exist'}")
                            )),
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the category is successfully deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(),
                                    examples = @ExampleObject(value = "{'Successfully deleted'}" )
                            ))
            }
    )
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "BearerToken")
    public ResponseEntity<?> deleteCategory(@Parameter(name = "id", description = "Id of the category to be deleted", example = "1")
                                            @PathVariable Integer id) {

        if (!adCategoryService.doesCategoryExist(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Category does not exist"));
        } else if (adCategoryService.findCategoryById(id).getAds().isEmpty()) {
            adCategoryService.deleteById(id);
            return ResponseEntity.ok().body("Successfully deleted");
        } else {
            if (!adCategoryService.doesCategoryExist("Uncategorized")){
                adCategoryService.createCategory(new AdCategoryDTO("Uncategorized", "Other"))
                        .setAds(adCategoryService.findCategoryById(id).getAds());
                adCategoryService.deleteById(id);
                return ResponseEntity.ok().body("Successfully deleted");
            }
            adCategoryService.findCategoryByName("Uncategorized")
                    .setAds(adCategoryService.findCategoryById(id).getAds());
            adCategoryService.deleteById(id);
            return ResponseEntity.ok().body("Successfully deleted");
        }
    }
}
