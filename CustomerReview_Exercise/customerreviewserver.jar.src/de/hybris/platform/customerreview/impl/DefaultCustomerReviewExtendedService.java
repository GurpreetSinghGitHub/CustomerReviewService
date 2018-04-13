/*    */ package de.hybris.platform.customerreview.impl;
/*    */ 
/*    */ import de.hybris.platform.core.model.c2l.LanguageModel;
/*    */ import de.hybris.platform.core.model.product.ProductModel;
/*    */ import de.hybris.platform.core.model.user.UserModel;
/*    */ import de.hybris.platform.customerreview.CustomerReviewService;
/*    */ import de.hybris.platform.customerreview.constants.CustomerReviewConstants;
/*    */ import de.hybris.platform.customerreview.dao.CustomerReviewDao;
/*    */ import de.hybris.platform.customerreview.jalo.CustomerReview;
/*    */ import de.hybris.platform.customerreview.jalo.CustomerReviewManager;
/*    */ import de.hybris.platform.customerreview.model.CustomerReviewModel;
/*    */ import de.hybris.platform.customerreview.validate.CustomerReviewValidator;
/*    */ import de.hybris.platform.customerreview.validate.impl.GeneratedCustomerReviewValidator;
/*    */ import de.hybris.platform.jalo.product.Product;
/*    */ import de.hybris.platform.jalo.user.User;
/*    */ import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
/*    */ import de.hybris.platform.servicelayer.model.ModelService;
/*    */ import de.hybris.platform.servicelayer.util.ServicesUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.Required;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ // Public service class that extends functionalities of parent service class
/*    */ // This class could have also been defined in a separate package
/*    */ public class DefaultCustomerReviewExtendedService
/*    */   extends DefaultCustomerReviewService
/*    */ {
/*    */   
/*    */   private CustomerReviewDao customerReviewDao;
/*    */   
/*    */   protected CustomerReviewDao getCustomerReviewDao()
/*    */   {
/*    */     return this.customerReviewDao;
/*    */   }
/*    */   
/*    */   @Required
/*    */   public void setCustomerReviewDao(CustomerReviewDao customerReviewDao)
/*    */   {
/*    */     this.customerReviewDao = customerReviewDao;
/*    */   }
/*    */
/*    */   // Method to get reviews that fall between a rating range defined by min and max 
/*    */   public Integer getNumberOfReviews(ProductModel product, double rangeMin, double rangeMax)
/*    */   {
/*    */     // Use existing function to get all reviews and then filter out the ones that do not fall in range to get a count	
/*    */     List<CustomerReview> result = CustomerReviewManager.getInstance().getAllReviews(product);
/*    */     Long totalCount = result.stream().filter(item->item.getRating()>=rangeMin && item.getRating()<=rangeMax).collect(Collectors.counting());
/*    */     return totalCount.intValue();	
/*    */   }
/*    */
/*    */   // Method to create a customer review after performing validations on input
/*    */   public CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, UserModel user, ProductModel product) throws JaloInvalidParameterException
/*    */   {
/*    */ 	
/*    */     // Ideally validator should be obtained from a validator factory; creating an object for exercise sake
/*    */     // If validator does not throw validation exception, create the customer review. The calling client should handle the exception
/*    */     CustomerReviewValidator validator = new GeneratedCustomerReviewValidator(comment, rating); 
/*    */     validator.validate();
/*    */
/*    */     CustomerReview review = CustomerReviewManager.getInstance().createCustomerReview(rating, headline, comment, 
/*    */       (User)getModelService().getSource(user), (Product)getModelService().getSource(product));
/*    */     return (CustomerReviewModel)getModelService().get(review);
/*    */   }
/*    */ 
/*    */ }