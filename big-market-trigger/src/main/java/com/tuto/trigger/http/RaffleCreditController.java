package com.tuto.trigger.http;

import com.tuto.domain.activity.service.IRaffleActivitySkuStockService;
import com.tuto.domain.credit.model.entity.TradeEntity;
import com.tuto.domain.credit.model.valobj.TradeNameVO;
import com.tuto.domain.credit.model.valobj.TradeTypeVO;
import com.tuto.domain.credit.service.ICreditAdjustService;
import com.tuto.domain.strategy.service.IRaffleStock;
import com.tuto.trigger.api.IRaffleActivityService;
import com.tuto.trigger.api.IRaffleCreditService;
import com.tuto.trigger.api.dto.SubtractCreditRequestDTO;
import com.tuto.types.enums.ResponseCode;
import com.tuto.types.exception.AppException;
import com.tuto.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author tu
 * @date 2025-01-08 下午5:00
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/credit")
public class RaffleCreditController implements IRaffleCreditService {

    @Resource
    private ICreditAdjustService  creditAdjustService;

    @RequestMapping(value = "subtract_credit", method = RequestMethod.POST)
    @Override
    public Response<Boolean> subtractCredit(@RequestBody SubtractCreditRequestDTO request) {
        try {
            log.info("扣减积分开始 userId: {} amount: {} outBusinessNo: {}", request.getUserId(), request.getAmount(), request.getOutBusinessNo());

            TradeEntity tradeEntity = TradeEntity.builder()
                    .userId(request.getUserId())
                    .amount(request.getAmount().multiply(new BigDecimal(-1)))
                    .tradeName(TradeNameVO.CONVERT_SKU)
                    .outBusinessNo(request.getOutBusinessNo())
                    .tradeType(TradeTypeVO.REVERSE)
                    .build();
            String order = creditAdjustService.createOrder(tradeEntity);
            if (order == null) {
                log.error("扣减积分失败 userId:{} amount:{} outBusinessNo:{}", request.getUserId(), request.getAmount(), request.getOutBusinessNo());
                return Response.<Boolean>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info(ResponseCode.UN_ERROR.getInfo())
                        .build();
            }
            log.info("扣减积分成功 userId:{} amount:{} outBusinessNo:{}", request.getUserId(), request.getAmount(), request.getOutBusinessNo());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();

        } catch (AppException e){
            log.error("扣减积分失败 userId:{} amount:{} outBusinessNo:{}", request.getUserId(), request.getAmount(), request.getOutBusinessNo(), e);
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("扣减积分失败 userId:{} amount:{} outBusinessNo:{}", request.getUserId(), request.getAmount(), request.getOutBusinessNo(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
