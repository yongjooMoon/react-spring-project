package com.msaProjectMenu01.menu.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.msaProjectMenu01.core.util.Util;
import com.msaProjectMenu01.menu.model.Product;
import com.msaProjectMenu01.menu.repository.ProductRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RefreshScope
public class ProductService {

	@Value("${upload.path}")
	private String uploadFilePath;
	
	@Autowired
	private ProductRepository productRepository;
	
	// 상품조회
	public ResponseEntity<Map<String, Object>> getAllProducts() throws IOException {
		Map<String, Object> result = new HashMap<>();
		
		List<Product> list = productRepository.findAll();
		List<Product> resultList = new ArrayList<Product>();
		
		for(Product product : list) {
			String filePath = product.getFilePath();
			
			Product param = new Product();
			param.setProductId(product.getProductId());
			param.setProductNm(product.getProductNm());
			param.setAtm(product.getAtm());
			
			if(!"".equals(filePath)) {
				byte[] fileByte = Util.convertFileToByteArray(filePath);
				param.setImageData(fileByte);
			}
			
			
			resultList.add(param);
		}
		
		result.put("list", resultList);
		
		return ResponseEntity.ok(result);
	}
	
	// 상품등록
	public void createProduct(HttpServletRequest request) throws Exception {
		String productName = request.getParameter("productName");
		String productPrice = request.getParameter("productPrice");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile imageFile = multipartRequest.getFile("imageFile");
		String userId = request.getParameter("userId");
		
		// 이미지 파일 처리
        if (imageFile == null) {
            // 이미지 파일 저장 또는 처리
        	throw new Exception("이미지 미존재");
        }
		
		String orginFileNm = imageFile.getOriginalFilename();
        String fileNm = Util.getUuid("", orginFileNm);
        String ext = "";
        if(orginFileNm.indexOf(".") > -1) {
        	ext = orginFileNm.substring(orginFileNm.indexOf(".")); 
        }
        
        // 디레토리가 없다면 생성
 		File dir = new File(uploadFilePath);
 		if (!dir.isDirectory()) {
 			dir.mkdirs();
 		}
        
        String saveFilePath = uploadFilePath + File.separator + fileNm + ext;
        
        File serverFile = new File(saveFilePath);
        imageFile.transferTo(serverFile);
        
        Product param = new Product();
        param.setProductNm(productName);
        param.setAtm(productPrice);
        param.setFileNm(fileNm);
        param.setOriginalFileNm(orginFileNm + ext);
        param.setFilePath(saveFilePath);
        param.setUserId(userId);
        
        
        productRepository.save(param);
	}
	
	// 상품삭제
	public void deleteProduct(Integer productId) {
		
		Optional<Product> product = productRepository.findById(productId);
		
		if (product.isPresent()) {
			// 이미지 삭제
			File file = new File(product.get().getFilePath());
			
			if(file.exists()) {
				file.delete();
			}			
			
			productRepository.deleteById(productId);
		}
	}
	
	// 상품 수정
	/*
	 * 기존 파일 삭제 -> 신규 파일 등록
	 * */
	public void productUpdate(HttpServletRequest request) throws Exception {
		String productName = request.getParameter("productName");
		String productPrice = request.getParameter("productPrice");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile imageFile = multipartRequest.getFile("imageFile");
		String userId = request.getParameter("userId");
		Integer productId = Integer.parseInt(request.getParameter("productId"));
		
		Optional<Product> productOptional = productRepository.findById(productId);

		if (productOptional.isPresent()) {
		    Product existingProduct = productOptional.get();
		    
		    // 기존 데이터를 유지하면서 필요한 부분만 수정
		    Product param = new Product();
		    param.setProductId(existingProduct.getProductId());
		    param.setProductNm(productName);
		    param.setAtm(productPrice);
		    param.setUserId(userId);
		    
		    // 파일이 존재하면 파일 관련 필드를 업데이트
		    if (imageFile != null && !imageFile.isEmpty()) {
		        // 기존 이미지 삭제
		        File file = new File(existingProduct.getFilePath());
		        if (file.exists()) {
		            file.delete();
		        }

		        String originFileNm = imageFile.getOriginalFilename();
		        String fileNm = Util.getUuid("", originFileNm);
		        String ext = "";

		        if (originFileNm != null && originFileNm.contains(".")) {
		            ext = originFileNm.substring(originFileNm.lastIndexOf("."));
		        }

		        // 디렉토리가 없다면 생성
		        File dir = new File(uploadFilePath);
		        if (!dir.exists()) {
		            dir.mkdirs();
		        }

		        String saveFilePath = uploadFilePath + File.separator + fileNm + ext;
		        File serverFile = new File(saveFilePath);
		        imageFile.transferTo(serverFile);

		        param.setFileNm(fileNm);
		        param.setOriginalFileNm(originFileNm + ext);
		        param.setFilePath(saveFilePath);
		    } else {
		        // 파일이 없으면 기존 파일 정보를 유지
		        param.setFileNm(existingProduct.getFileNm());
		        param.setOriginalFileNm(existingProduct.getOriginalFileNm());
		        param.setFilePath(existingProduct.getFilePath());
		    }

		    productRepository.save(param);
		} else {
		    // 제품이 없을 경우 처리
		    throw new RuntimeException("수정할 데이터가 미존재합니다.: " + productId);
		}

	}
}
