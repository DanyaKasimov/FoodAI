import {Injectable} from '@nestjs/common';
import * as fs from "fs";
import {OpenaiClient} from "./client/openai.client";
import * as path from "node:path";

@Injectable()
export class AppService {

    constructor(private readonly openaiClient: OpenaiClient) {
    }

    public async analyzePhoto(path: string): Promise<string> {
        const image: string = this.getPhoto(path);
        return this.openaiClient.analyzeImage(image);
    }

    private getPhoto(fileName: string): string {
        const rootDir = process.cwd() + "/../";
        const fullPath = path.join(rootDir, fileName);
        return fs.readFileSync(fullPath, { encoding: 'base64' });
    }
}
