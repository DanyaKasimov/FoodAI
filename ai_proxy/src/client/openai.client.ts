import {Injectable} from "@nestjs/common";
import OpenAI from "openai";
import {ApiClientException} from "../exception/api.client.exception";

@Injectable()
export class OpenaiClient {
    private readonly openai: OpenAI;

    private static readonly MODEL: string = "gpt-4o-mini"

    private static readonly BASE_URL: string = "https://api.proxyapi.ru/openai/v1";

    private static readonly PROMPT: string = "Определи блюдо по фото, определи примерный вес, а также его КБЖУ. Ответь в таком формате и ничего более:" +
        "Блюдо на фото: " +
        "Примерный вес: " +
        "Количество калорий: " +
        "Количество белков: " +
        "Количество углеводов: " +
        "Количество жиров: ";

    private static readonly MAX_TOKENS: number = 1000;

    constructor() {
        this.openai = new OpenAI({
            apiKey: "",
            baseURL:OpenaiClient.BASE_URL
        });
    }

    public async analyzeImage(imageBase64: string): Promise<string> {
        const response = await this.openai.chat.completions.create({
            model: OpenaiClient.MODEL,
            messages: [
                {
                    role: "user",
                    content: [
                        {
                            type: "text",
                            text: OpenaiClient.PROMPT
                        },
                        {
                            type: "image_url",
                            image_url: {
                                url: `data:image/jpeg;base64,${imageBase64}`
                            }
                        }
                    ]
                }
            ],
            max_tokens: OpenaiClient.MAX_TOKENS
        });

        const content: string | null = response.choices[0]?.message?.content;

        if (!content) throw new ApiClientException("Пустой ответ от OpenAI");

        return content;
    }
}