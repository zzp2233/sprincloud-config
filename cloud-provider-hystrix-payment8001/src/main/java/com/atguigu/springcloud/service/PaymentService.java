package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.micrometer.core.instrument.step.StepCounter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    public String paymentInfo_OK(Integer id)
    {
        return "线程池  "+Thread.currentThread().getName()+"  paymentInfo_OK,id"+id+"\t"+"O(∩_∩)O哈哈";
    }


    //@HystrixCommand: 指定该程序出错时兜底的方法
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",
            commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")//该线程的时间是5秒,超过就出错
            }
    )
    public String paymentInfo_TimeOut(Integer id){
        int tinmeNumber = 3;  //规定运行睡眠时间，运行超时异常
        //int age =10/0  //程序报错
        try {
            TimeUnit.SECONDS.sleep(tinmeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池  "+Thread.currentThread().getName()+"  paymentInfo_TimeOut"+id+"\t"+"O(∩_∩)O哈哈";
    }


    public String paymentInfo_TimeOutHandler(Integer id)
    {
        return "线程池： "+Thread.currentThread().getName()+" 系统繁忙或者运行报错，请稍后再试 ,id: "+id+"\t"+"~~~~(>_<)~~~~兜底啦";
    }


    //=====服务熔断    commandProperties:数组的属性
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),// 失败率达到多少后跳闸
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), // 时间窗口期/时间范围，短路后多久开始尝试恢复
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id)
    {
        if(id < 0)
        {
            throw new RuntimeException("******id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID(); //simpleUUID：hutool工具包的一个方法，返回不带 - 的唯一标识码

        return Thread.currentThread().getName()+"\t"+"调用成功，流水号: " + serialNumber;
    }



    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id)
    {
        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~   id: " +id;
    }

}

























