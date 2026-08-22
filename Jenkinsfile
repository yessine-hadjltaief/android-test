pipeline {
    agent any

    stages {

        stage('Check Environment') {
            steps {
                sh '''
                    echo "User: $(whoami)"
                    echo "ANDROID_HOME=$ANDROID_HOME"
                    java -version
                    adb version
                '''
            }
        }

        stage('Build APK') {
            steps {
                sh '''
                    chmod +x gradlew
                    ./gradlew clean assembleDebug
                '''
            }
        }

        stage('Check Cuttlefish') {
            steps {
                sh '''
                    adb wait-for-device
                    adb devices
                '''
            }
        }

        stage('Install APK') {
            steps {
                sh '''
                    adb install -r app/build/outputs/apk/debug/app-debug.apk
                '''
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk',
                             fingerprint: true
        }
    }
}
