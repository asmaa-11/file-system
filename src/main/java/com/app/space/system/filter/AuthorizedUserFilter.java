package com.app.space.system.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import com.app.space.system.entity.Item;
import com.app.space.system.entity.ItemDetails;
import com.app.space.system.entity.Permission;
import com.app.space.system.entity.PermissionGroup;
import com.app.space.system.repository.ItemDetailsRepository;
import com.app.space.system.repository.ItemRepository;
import com.app.space.system.repository.PermissionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Optional;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthorizedUserFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizedUserFilter.class);
	@Autowired
	PermissionRepository permissionRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ItemDetailsRepository itemDetailsRepository;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("########## Initiating Custom filter ##########");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		LOGGER.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());
		/* get user authority from header */
		String userEmail = request.getHeader("useremail");
		if(userEmail==null) {
			constructErrorResponse(response, 404, "useremail required");
			return;
		}
		Permission per = permissionRepository.findByUserEmail(userEmail);
		if (per == null) {
			// throw usernemail not found
			constructErrorResponse(response,404, "user not found");
			return;
		}
		/* get */
		String servletPath = request.getServletPath(); // /servlet/MyServlet
		if (servletPath.contains("/create") || servletPath.contains("/upload")) {
			/* check if requested user has privilege to create */
			if (!per.getPermissionLevel().equalsIgnoreCase("EDIT")) {
				// THROW UNAUTHORIZED USER
				constructErrorResponse(response, 401," UNAUTHORIZED USER");
				return;

			}
		} else if (servletPath.contains("/getFileMetaData/")) {
			String fileId = servletPath.substring(servletPath.lastIndexOf("/")+1, servletPath.length());
			Optional<Item> itemOptional = itemRepository.findById(Integer.valueOf(fileId));
			if (itemOptional.isPresent()) {
				PermissionGroup pg = itemOptional.get().getPermissionGroup();
				if (pg.getId() != per.getPermissionGroup().getId()) {
					// THROW UNAUTHORIZED USER
					constructErrorResponse(response,401, " UNAUTHORIZED USER");
					return;

				}

			} else {
				constructErrorResponse(response,404 ," item not found");
				return;

			}

		} else if (servletPath.contains("/download/")) {
			/* get file permissionGroup */
			String fileId = servletPath.substring(servletPath.lastIndexOf("/")+1, servletPath.length());
			Optional<ItemDetails> pg = itemDetailsRepository.findById(Integer.valueOf(fileId));

			if (pg.isPresent()) {
				if (pg.get().getItem().getPermissionGroup().getId() != per.getPermissionGroup().getId()) {
					// THROW UNAUTHORIZED USER
					constructErrorResponse(response,401, " UNAUTHORIZED USER");
					return;

				}
			} else {
				constructErrorResponse(response, 404," item not found");
				return;

			}
		}
		// call next filter in the filter chain
		filterChain.doFilter(request, response);

		LOGGER.info("Logging Response :{}", response.getContentType());
	}

	void constructErrorResponse(HttpServletResponse res,int status, String error) throws IOException {

		// set the response object
		res.setStatus(status);
		res.setContentType("application/json");

		PrintWriter out = res.getWriter();
		out.print(error);
		out.flush();
	}

	@Override
	public void destroy() {

	}
}