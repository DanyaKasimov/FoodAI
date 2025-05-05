import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import {OpenaiClient} from "./client/openai.client";

@Module({
  imports: [],
  controllers: [AppController],
  providers: [AppService, OpenaiClient],
})
export class AppModule {}
