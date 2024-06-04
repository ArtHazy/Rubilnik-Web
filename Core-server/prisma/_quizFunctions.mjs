import { PrismaClient } from '@prisma/client';
const prisma = new PrismaClient();

export async function updateUsersQuizzes(userId,quizzes) {
    return await prisma.user.update({where: { id: userId},data: {quizzes: quizzes},});
}