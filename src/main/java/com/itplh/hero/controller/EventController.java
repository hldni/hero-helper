package com.itplh.hero.controller;

import com.itplh.hero.constant.ParameterEnum;
import com.itplh.hero.domain.HeroRegionUser;
import com.itplh.hero.domain.SimpleUser;
import com.itplh.hero.event.HeroEventContext;
import com.itplh.hero.event.core.NPCFixedEvent;
import com.itplh.hero.listener.EventBus;
import com.itplh.hero.service.HeroRegionUserService;
import com.itplh.hero.util.EventTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventBus eventBus;

    @Autowired
    private HeroRegionUserService heroRegionUserService;

    @PostMapping("/trigger")
    public Result eventTrigger(@RequestParam(required = true) String eventName,
                               @RequestParam(required = false) String sid,
                               @RequestParam(required = false, defaultValue = "1") Long targetRunRound,
                               @RequestBody Map<String, String> extendInfo) {
        sid = setDefaultSidIfAbsent(sid);

        Result validate = validate(eventName, sid, extendInfo);
        if (!validate.isSuccess()) {
            return validate;
        }

        log.info("start trigger [sid={}] [eventName={}]", sid, eventName);
        SimpleUser user = heroRegionUserService.get(sid).get().simpleUser();
        HeroEventContext heroEventContext = HeroEventContext.newInstance(user, eventName, targetRunRound, extendInfo);
        boolean isSuccess = EventTemplateUtil.getEventInstance(eventName, heroEventContext)
                .map(event -> eventBus.publishEvent(event))
                .orElse(false);
        log.info("finish trigger [sid={}] [eventName={}] [isSuccess={}]", sid, eventName, isSuccess);
        return Result.ok(String.format("finish trigger [sid=%s] [eventName=%s]  [isSuccess=%s]", sid, eventName, isSuccess));
    }

    @PostMapping("/close")
    public Result eventClose(String sid) {
        sid = setDefaultSidIfAbsent(sid);

        log.info("start close [sid={}]", sid);
        boolean isSuccess = eventBus.close(sid);
        log.info("finish close [sid={}] [isSuccess={}]", sid, isSuccess);
        return Result.ok(String.format("[sid=%s] [isSuccess=%s]", sid, isSuccess));
    }

    @PostMapping("/close-all")
    public Result eventCloseAll() {
        log.info("start close all");
        int closeCounter = eventBus.closeAll();
        log.info("finish close all [total closed={}]", closeCounter);
        return Result.ok(String.format("finish close all [total closed=%s]", closeCounter));
    }

    @PostMapping("/pause")
    public Result eventPause(String sid) {
        sid = setDefaultSidIfAbsent(sid);

        log.info("start pause [sid={}]", sid);
        boolean isSuccess = eventBus.pause(sid);
        log.info("finish pause [sid={}] [isSuccess={}]", sid, isSuccess);
        return Result.ok(String.format("[sid=%s] [isSuccess=%s]", sid, isSuccess));
    }

    @PostMapping("/pause-all")
    public Result eventPauseAll() {
        log.info("start pause all");
        int pauseCounter = eventBus.pauseAll();
        log.info("finish pause all [total pause={}]", pauseCounter);
        return Result.ok(String.format("finish pause all [total paused=%s]", pauseCounter));
    }

    @PostMapping("/restart")
    public Result eventRestart(String sid) {
        sid = setDefaultSidIfAbsent(sid);

        log.info("start restart [sid={}]", sid);
        boolean isSuccess = eventBus.restart(sid);
        log.info("finish restart [sid={}] [isSuccess={}]", sid, isSuccess);
        return Result.ok(String.format("[sid=%s] [isSuccess=%s]", sid, isSuccess));
    }

    @PostMapping("/restart-all")
    public Result eventRestartAll() {
        log.info("start restart all");
        int restartCounter = eventBus.restartAll();
        log.info("finish restart all [total restarted={}]", restartCounter);
        return Result.ok(String.format("finish restart all [total restarted=%s]", restartCounter));
    }

    @GetMapping("/get")
    public Result eventOne(String sid) {
        sid = setDefaultSidIfAbsent(sid);
        return Result.ok(eventBus.getEvent(sid));
    }

    @GetMapping("/get-all")
    public Result eventAll() {
        return Result.ok(eventBus.getAllEvent());
    }

    private String setDefaultSidIfAbsent(String sid) {
        if (StringUtils.isEmpty(sid)) {
            Optional<HeroRegionUser> first = heroRegionUserService.getFirst();
            if (first.isPresent()) {
                sid = first.get().getSid();
            }
        }
        return sid;
    }

    private Result validate(String eventName,
                            String sid,
                            Map<String, String> extendInfo) {
        if (!heroRegionUserService.contains(sid)) {
            return Result.error("sid is invalid.");
        }
        if (StringUtils.isEmpty(eventName)) {
            return Result.error("eventName is required.");
        }
        if (!EventTemplateUtil.hasEventInstance(eventName)) {
            return Result.error("eventName is invalid.");
        }
        if (Objects.equals(eventName, NPCFixedEvent.class.getSimpleName())) {
            String resource = ParameterEnum.RESOURCE.getName();
            if (CollectionUtils.isEmpty(extendInfo) || !extendInfo.containsKey(resource)) {
                return Result.error(String.format("%s must set %s", NPCFixedEvent.class.getSimpleName(), resource));
            }
        }
        return Result.ok();
    }

}


