import { PrismaClient } from '@prisma/client';
const prisma = new PrismaClient();

export async function deleteAllUsers() {
  return await prisma.user.deleteMany();
}
async function findUser(email) {
  console.log('find '+email);
  return await prisma.user.findUnique({ where: { email } })
}

export async function createUser(id,{name,email,password},quizzes) {
  let user = await findUser(email)
  if (user){return null}
  else{ return await prisma.user.create({data: {id,email,name,password,quizzes}})}
}
export async function deleteUserById(id) {
  return await prisma.user.delete({where: {id}});
}
export async function getUser(email) {
  return await prisma.user.findUnique({where:{email}})
}
export async function putUser(email, {name,password,quizzes}) {
  let user = await findUser(email);
  console.log(user);
  if(user){
    return await prisma.user.update({where:{email}, data: {email,name,password,quizzes}})
  } else return null
}


