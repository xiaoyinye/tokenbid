const aws = require('aws-sdk');
const config = require('./config');

const region = config.region;
const bucketName = config.bucketName;
const accessKeyId = config.accessKeyId;
const secretAccessKey = config.secretAccessKey;

const s3 = new aws.S3({
  region,
  accessKeyId,
  secretAccessKey,
  signatureVersion: 'v4',
});

export async function generateUploadUrl(randomImageName) {
  const uploadUrl = s3.getSignedUrl('putObject', {
    Bucket: bucketName,
    Key: randomImageName,
    Expires: 60,
  });

  return uploadUrl;
}

export async function uploadImage(uploadUrl, image) {
  const response = await fetch(uploadUrl, {
    method: 'PUT',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    body: image,
  });

  return response;
}
