package com.olx.sac.sacwebbff.service;

import java.util.List;

import com.olx.sac.sacserviceapi.attendance.StateDTO;
import com.olx.sac.sacwebbff.exception.CallServiceException;

public interface StateService {

	public StateDTO findOnByUf(String uf) throws CallServiceException;

	public List<StateDTO> findAll() throws CallServiceException;
}
