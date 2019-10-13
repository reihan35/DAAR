import os
import matplotlib.pyplot as plt
import numpy as np

B10K = "../resultsRapport/10K"
B100K = "../resultsRapport/100K"
B1M = "../resultsRapport/1M"
NBFILE = 100


def plot1_2Results(title, ylabel, data, var=True, path='', percentage=False):
    # ('M3', 'M2', 'Egrep')

    n_groups = 3
    fig, ax = plt.subplots()
    index = np.arange(n_groups)
    bar_width = 0.15

    opacity = 0.3
    error_config = {'ecolor': '0.3'}

    rects1 = plt.bar(index - bar_width, data["B10K"], bar_width,
                     alpha=opacity,
                     color='b',
                     yerr=data["B10K_var"],
                     error_kw=error_config,
                     label='B10K')

    rects2 = plt.bar(index, data["B100K"], bar_width,
                     alpha=opacity,
                     color='r',
                     yerr=data["B100K_var"],
                     error_kw=error_config,
                     label='B100K')

    rects3 = plt.bar(index + bar_width, data["B1M"], bar_width,
                     alpha=opacity,
                     color='g',
                     yerr=data["B1M_var"],
                     error_kw=error_config,
                     label='B1M')

    if True:
        pass
        # plt.ylim(0, 1 * 10 ** 7)
        # plt.xlim(-1, 5)

    plt.xlabel('Algorithmes')
    plt.ylabel(ylabel)
    plt.title(title)
    plt.xticks(index + bar_width / 2, ('M3', 'KMP', 'Egrep'))
    plt.legend()

    plt.tight_layout()
    plt.savefig(title.replace(' ', '_').replace('\'', '') + '.png')

    plt.show()


def main1():
    # ysXXX = 'egrep', 'M3', 'M2'
    cmd = "awk -F ' ' '{sumEg += $4} {sumM3 += $6}{sumM2 += $8}END {print (sumEg/" + str(NBFILE) + " , sumM3/" + str(
        NBFILE) + ", sumM2/" + str(NBFILE) + ") }' " + B10K[1::]
    listData = os.popen(cmd).read().split('\n')[0]
    ys10k = list(map(int, listData.split(" ")))
    # print(ys10k)

    cmd = "awk -F ' ' '{sumEg += $4} {sumM3 += $6}{sumM2 += $8}END {print (sumEg/" + str(NBFILE) + " , sumM3/" + str(
        NBFILE) + ", sumM2/" + str(NBFILE) + ") }' " + B100K[1::]
    listData = os.popen(cmd).read().split('\n')[0]
    ys100k = list(map(int, listData.split(" ")))
    # print(ys100k)

    cmd = "awk -F ' ' '{sumEg += $4} {sumM3 += $6}{sumM2 += $8}END {print (sumEg/" + str(NBFILE) + " , sumM3/" + str(
        NBFILE) + ", sumM2/" + str(NBFILE) + ") }' " + B1M[1::]
    listData = os.popen(cmd).read().split('\n')[0]
    ys1M = list(map(int, listData.split(" ")))
    # print(ys1M)

    print("str(ys10k[0] * ys10k[0]) : ", str(ys10k[0] * ys10k[0]))

    cmd = "awk -F' ' '{sumEg+=($4*$4-" + str(ys10k[0] * ys10k[0]) + ")} " + \
          "{sumM3+=($6*$6-" + str(ys10k[1] * ys10k[1]) + ")} {sumM2+=($8*$8-" + str(ys10k[2] * ys10k[2]) + ")} " + \
          " END{print sqrt(sumEg/" + str(NBFILE) + "), sqrt(sumM3/" + str(NBFILE) + "), sqrt(sumM2/" + str(
        NBFILE) + ")}' " + B10K[1::]

    listData = os.popen(cmd).read().split('\n')[0]
    var10k = list(map(float, listData.split(" ")))

    cmd = "awk -F' ' '{sumEg+=($4*$4-" + str(ys100k[0] * ys100k[0]) + ")} " + \
          "{sumM3+=($6*$6-" + str(ys100k[1] * ys100k[1]) + ")} {sumM2+=($8*$8-" + str(ys100k[2] * ys100k[2]) + ")} " + \
          " END{print sqrt(sumEg/" + str(NBFILE) + "), sqrt(sumM3/" + str(NBFILE) + "), sqrt(sumM2/" + str(
        NBFILE) + ")}' " + B100K[1::]

    listData = os.popen(cmd).read().split('\n')[0]
    var100k = list(map(float, listData.split(" ")))

    cmd = "awk -F' ' '{sumEg+=($4*$4-" + str(ys1M[0] * ys1M[0]) + ")} " + \
          "{sumM3+=($6*$6-" + str(ys1M[1] * ys1M[1]) + ")} {sumM2+=($8*$8-" + str(ys1M[2] * ys1M[2]) + ")} " + \
          " END{print sqrt(sumEg/" + str(NBFILE) + "), sqrt(sumM3/" + str(NBFILE) + "), sqrt(sumM2/" + str(
        NBFILE) + ")}' " + B1M[1::]

    listData = os.popen(cmd).read().split('\n')[0]
    var1M = list(map(float, listData.split(" ")))

    data = {"B10K": (ys10k[1], ys10k[2], ys10k[0]),
            "B10K_var": (
                var10k[1],
                var10k[2],
                var10k[0]),
            "B100K": (ys100k[1], ys100k[2], ys100k[0]),
            "B100K_var": (
                var100k[1],
                var100k[2],
                var100k[0]),
            "B1M": (ys1M[1], ys1M[2], ys1M[0]),
            "B1M_var": (
                var1M[1],
                var1M[2],
                var1M[0]),
            }
    print(var1M)
    print(data)
    plot1_2Results("Temps d'exécution recherche d'un motif", "Times (ns)", data, var=False, path='Discussion/')


main1()
