package com.naftal.gmao.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestRestAPIs {
	

	@GetMapping("/api/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return ">>> Admin Contents";
	}

	@GetMapping("/api/test/magasinier")
	@PreAuthorize("hasRole('MAGASINIER')")
	public String magasinierAccess() {
		return ">>> Magasinier Contents";
	}

	@GetMapping("/api/test/cadre")
	@PreAuthorize("hasRole('CADRE')")
	public String cadreAccess() {
		return ">>> cadre Contents";
	}

	@GetMapping("/api/test/chefStation")
	@PreAuthorize("hasRole('CHEFSTATION')")
	public String chefStationAccess() {
		return ">>> ChefStation Contents";
	}

	@GetMapping("/api/test/intervenant")
	@PreAuthorize("hasRole('INTERVENANT')")
	public String intervenantAccess() {
		return ">>> intervenants Contents";
	}

	@GetMapping("/api/test/commercial")
	@PreAuthorize("hasRole('COMMERCIAL')")
	public String commercialAccess() {
		return ">>> Commercial Contents";
	}





}