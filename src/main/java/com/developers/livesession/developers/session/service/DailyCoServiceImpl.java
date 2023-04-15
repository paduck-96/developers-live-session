package com.developers.livesession.developers.session.service;

import com.developers.livesession.developers.exception.DailyCoException;
import com.developers.livesession.developers.session.dto.dailyco.DailyCoAnswer;
import com.developers.livesession.developers.session.dto.dailyco.DailyCoCreateRequest;
import com.developers.livesession.developers.session.dto.dailyco.DailyCoResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class DailyCoServiceImpl implements DailyCoService{
    @Override
    public DailyCoResponse create(DailyCoCreateRequest request) {
        try{
            // resttemplate 을 통한 처리
            RestTemplate restTemplate = new RestTemplate();
            String createRoomUrl = "https://api.daily.co/v1/rooms";

            // 인증 헤더 추가 필요
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer 17885ccd0a16f1c5e4d642075773a775fc45b46b020cfac4023c3fb88f7aba01");
            HttpEntity<DailyCoCreateRequest> entity = new HttpEntity<>(request, headers);


            // 상태 추가 확인을 위한 entity 처리
            //body 값만 필요하면 object를 쓰는데, 이 부분은 고민이 조금 됌
            ResponseEntity<DailyCoAnswer> responseEntity = restTemplate.postForEntity(createRoomUrl, entity, DailyCoAnswer.class);

            if(responseEntity.getStatusCode()==HttpStatus.OK){
                log.info("방 생성 성공! "+responseEntity.getBody());
                DailyCoResponse response = DailyCoResponse.builder()
                        .code(responseEntity.getStatusCode().toString())
                        .msg("방이 성공적으로 개설되었습니다!")
                        .data(responseEntity.getBody())
                        .build();
                return response;
            }else{
                log.error("방 생성 실패: "+responseEntity.getBody());
                throw new DailyCoException("방 생성이 실패하였습니다! "+responseEntity.getBody(), responseEntity.getStatusCode().value());
            }
        }catch (Exception e){
            log.error("방 생성 오류! ",e);
            throw new DailyCoException("dailyco에서 방 생성 오류가 발생하였습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public DailyCoResponse delete(String roomName) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String deleteRoomUrl = "https://api.daily.co/v1/rooms/" + roomName;

            restTemplate.delete(deleteRoomUrl);
            log.info("방 삭제 성공! " + roomName);
            DailyCoResponse response = DailyCoResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg("방이 성공적으로 삭제되었습니다!")
                    .data(null)
                    .build();
            return response;
        } catch (Exception e) {
            log.error("방 삭제 오류! ", e);
            throw new DailyCoException("dailyco에서 방 삭제 오류가 발생하였습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
