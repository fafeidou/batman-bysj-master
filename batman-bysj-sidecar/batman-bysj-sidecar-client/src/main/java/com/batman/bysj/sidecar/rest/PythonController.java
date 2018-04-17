package com.batman.bysj.sidecar.rest;

import com.batman.bysj.sidecar.client.PythonFeignClient;
import com.batman.bysj.sidecar.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author yangyongjie
 * @create 2017-10-22 20:30
 */
@RestController
@RequestMapping("test")
public class PythonController {
    @Autowired
    private PythonFeignClient pythonFeignClient;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String getTest() {
        return pythonFeignClient.getTest();
    }

    @RequestMapping(value = "/message/{id}", method = RequestMethod.GET)
    public List<Message> getMsg(@PathVariable Long id) {
        return pythonFeignClient.getMsg(id);
    }
}