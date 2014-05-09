package com.ffh.babblehouse.controller.BBNodes;
import com.ffh.babblehouse.model.DtoMessage;

public interface IMessage {
	// Methods BBNode offers
	DtoMessage processMessage (DtoMessage obj);
}
