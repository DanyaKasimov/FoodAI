import {Body, Controller, Get, HttpException, HttpStatus, Logger, Post} from '@nestjs/common';
import { AppService } from './app.service';
import {PhotoAnalyzeDto} from "./dto/photo.analyze.dto";
import {ApiClientException} from "./exception/api.client.exception";

@Controller("/ai")
export class AppController {
  private readonly logger = new Logger(AppController.name);

  constructor(private readonly appService: AppService) {}

  @Post("/photo-analyze")
  public async photoAnalyze(@Body() body: PhotoAnalyzeDto): Promise<string> {
    this.logger.log(`Поступил запрос на анализ фото. Файл: ${JSON.stringify(body)}`)

    try {
      return await this.appService.analyzePhoto(body.path);
    } catch (e) {
      if (e instanceof ApiClientException) {
        throw new HttpException(e.message, HttpStatus.BAD_REQUEST)
      }
      throw new HttpException(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }
}
