package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.AdCategory;
import com.blue.foxbuy.models.DTOs.AdCategoryDTO;
import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.services.AdCategoryService;
import com.blue.foxbuy.services.AdService;
import com.blue.foxbuy.services.ConversionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Tag(name = "Ad categories")
public class AdCategoryController {
    private final AdCategoryService adCategoryService;
    private final AdService adService;
    private final ConversionService conversionService;

    @Autowired
    public AdCategoryController(AdCategoryService adCategoryService, AdService adService, ConversionService conversionService) {
        this.adCategoryService = adCategoryService;
        this.adService = adService;
        this.conversionService = conversionService;
    }

    @Operation(
            description = "An endpoint that lists all the available ads in a given category." +
                    " If the URL contains an 'empty=true' parameter, the endpoint lists all ad" +
                    " categories and their respective ads.",
            summary = "Lists ad categories and ads.",
            method = "GET",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Returned if the URL parameter is not a true or false statement.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"404\"," +
                                            "\"message\": \"Invalid value for the 'empty' parameter. Only 'true' or 'false' are allowed.\"" +
                                            "}")
                            )),
                    @ApiResponse(responseCode = "200",
                            description = "Returned on successful request.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AdCategoryDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "categories: " +
                                            "{“name”: “IT”, " +
                                            "“description”: “PC, MOBILE, and all IT stuff” " +
                                            "“ads”: 3}," +
                                            "{“name”: “Clothes” " +
                                            "“description”: “Shirts, Pants and others”" +
                                            "“ads”: 1}" +
                                            "}")
                            ))
            }
    )
    @GetMapping
    public ResponseEntity<?> listAdCategories(@RequestParam(name = "empty", defaultValue = "false") String empty) throws JsonProcessingException {

        if (!empty.equalsIgnoreCase("true") && !empty.equalsIgnoreCase("false")) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("404");

            errorDTO.setMessage("Invalid value for 'empty' parameter. Only 'true' or 'false' are allowed.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }

        if (empty.equals("false")) {
            List<AdCategory> categories = adCategoryService.findAllCategoriesWithAds();
            String categoriesJson = conversionService.convertObjectToJson(categories);

            return ResponseEntity.ok().body(categoriesJson);
        } else {
            List<AdCategory> categories = adCategoryService.findAll();
            String categoriesJson = conversionService.convertObjectToJson(categories);

            return ResponseEntity.ok().body(categoriesJson);
        }
    }

    @Hidden
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody AdCategoryDTO adCategoryDTO) {

        if (adCategoryService.doesCategoryExist(adCategoryDTO.getName())) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("409");

            errorDTO.setMessage("Category already exists.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
        } else {
            AdCategory createdCategory = adCategoryService.saveAdCategoryDTO(adCategoryDTO);

            adCategoryDTO.setId(createdCategory.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(adCategoryDTO);
        }
    }

    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody AdCategoryDTO adCategoryDTO, @PathVariable(value = "id") String idString) {
        Integer id;

        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {

            throw new NumberFormatException("Categories use integers as their identifiers. Please provide a valid integer.");
        }

        AdCategory updatedCategory;
        if (!adCategoryService.doesCategoryExist(id)) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("404");

            errorDTO.setMessage("Category does not exist.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        } else {
            updatedCategory = adCategoryService.updateCategory(id, adCategoryDTO);

            adCategoryDTO.setId(updatedCategory.getId());

            return ResponseEntity.ok().body(adCategoryDTO);
        }
    }

    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {

        // Checking if the category exists and sending a 404 if it doesn't
        if (!adCategoryService.doesCategoryExist(id)) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("404");

            errorDTO.setMessage("Category does not exist.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
            // Checking if the category has any ads associated with it, if not, deleting it
        } else if (adCategoryService.findCategoryById(id).getAds().isEmpty()) {
            AdCategory adCategory = adCategoryService.findCategoryById(id);

            adCategoryService.deleteById(id);

            return ResponseEntity.ok().body(adCategory.getName() + " category successfully deleted.");
        } else {

            // Checking if the category called 'Uncategorized' exists, if it doesn't
            // we create it
            if (!adCategoryService.doesCategoryExist("Uncategorized")) {

                adCategoryService.saveAdCategoryDTO(new AdCategoryDTO("Uncategorized", "Other"));
            }
            // We get the ads associated with the category, bring in the 'Uncategorized'
            // ad category and then iterate through the ads belonging to the category
            // we want to delete and re-categorize them as 'Uncategorized'. Then we delete
            // the ad category.
            List<Ad> ads = adCategoryService.findCategoryById(id).getAds();
            AdCategory adCategory = adCategoryService.findCategoryByName("Uncategorized");

            for (Ad ad : ads) {
                ad.setAdCategory(adCategory);
                adService.saveAd(ad);
            }

            adCategoryService.deleteById(id);

            return ResponseEntity.ok().body(adCategory.getName() + "category successfully deleted.");
        }
    }
}